# home-network-monitor

Monitoramento de inventário de dispositivos e atividade de DNS para uma rede doméstica. Coleta quem está conectado na rede (via o roteador) e quais domínios cada dispositivo consulta (via o AdGuard Home), cruza as duas informações por MAC/IP e guarda o histórico.

## O que ele faz

- Lê periodicamente o painel web do roteador (VSOL V2802AC) e descobre os dispositivos conectados: MAC, hostname, IP, banda Wi-Fi (2.4G/5G), RSSI e contadores de pacotes.
- Lê o log de consultas DNS do AdGuard Home e grava cada consulta (domínio, horário, se foi bloqueada), já vinculada ao dispositivo correspondente pelo IP.
- Guarda tudo em um banco local persistente, com histórico ao longo do tempo (não só o estado atual).
- Expõe endpoints simples para consultar os dados e dar nomes amigáveis aos dispositivos.

## Arquitetura

```
provider/   -> fontes de dados externas (hoje: VsolDeviceProvider)
client/     -> clientes de API externas (hoje: AdguardQueryLogClient)
agent/      -> orquestra os providers de dispositivo
service/    -> upsert/ingestão nos dados coletados
scheduler/  -> dispara as coletas periodicamente
repository/ -> acesso ao banco (Spring Data JPA)
controller/ -> endpoints REST
models/     -> entidades, DTOs e enums
```

O `VsolDeviceProvider` autentica sozinho no painel do roteador quando a sessão expira (o VSOL rastreia sessão por IP de origem, sem cookie) e faz retry em caso de instabilidade do servidor embarcado do roteador.

## Stack

Java 21, Spring Boot 4.1.2, Spring Data JPA, H2 (arquivo local), Jsoup (parsing de HTML).

## Configuração

Variáveis de ambiente (ver `.env.example`):

| Variável | Obrigatória | Descrição |
|---|---|---|
| `VSOL_BASE_URL` | não (default `http://192.168.1.1`) | URL base do roteador |
| `VSOL_USERNAME` | sim, para o coletor de dispositivos funcionar | usuário de login do roteador |
| `VSOL_PASSWORD` | sim | senha de login do roteador |
| `ADGUARD_BASE_URL` | sim, para a ingestão de DNS funcionar | URL base do AdGuard Home |
| `ADGUARD_USERNAME` | sim | usuário de acesso ao AdGuard Home |
| `ADGUARD_PASSWORD` | sim | senha de acesso ao AdGuard Home |
| `COLLECTOR_INTERVAL_MS` | não (default 300000) | intervalo da coleta de dispositivos |
| `DNSLOG_INTERVAL_MS` | não (default 60000) | intervalo da ingestão de DNS |

Sem `ADGUARD_BASE_URL`/`VSOL_USERNAME` configurados, as respectivas integrações ficam desligadas (não quebram a aplicação).

## Como rodar

```bash
cp .env.example .env
# edita o .env com os valores reais
docker compose up -d --build
```

## Endpoints

- `GET /api/devices` — lista os dispositivos conhecidos (MAC, hostname, último IP, banda, RSSI, última vez visto).
- `PUT /api/devices/{mac}/name` — define um nome amigável para um dispositivo (body `{"name": "..."}`).
- `GET /api/dns-activity` — últimas 200 consultas DNS, já cruzadas com o dispositivo (IP, MAC, nome, domínio, se foi bloqueada, horário). Aceita `?mac=` para filtrar por dispositivo.

## Limitações conhecidas

- O cruzamento dispositivo↔consulta DNS é feito por IP atual; se um dispositivo trocar de IP entre coletas, consultas feitas nesse intervalo podem não ser vinculadas corretamente.
- `adguard.querylog-fetch-limit` (padrão 500) limita quantas consultas são lidas por ciclo; em redes com tráfego muito alto, parte do log pode não ser capturada entre um ciclo e outro.
- Integração com Home Assistant (presença, alertas) e dashboards no Grafana ainda não foram implementados.
