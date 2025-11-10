# AppClima

Aplicativo Android (Java) que consulta a previsão do tempo via API do HG Brasil e apresenta:
- Aba "Clima" com temperatura atual, umidade e lista de previsão (RecyclerView)
- Aba "Mapa" (placeholder)
- Tela "Sobre" acessível pelo menu da toolbar

Este projeto foi criado com AndroidX, Material Components, OkHttp e Gson.

## Sumário
- Descrição e objetivo
- Arquitetura do app
- Requisitos
- Configuração e execução
- Estrutura de diretórios
- Fluxos principais
- Dependências


## Descrição e objetivo
O AppClima consome a API pública do HG Brasil para exibir informações de clima de um local pré-definido (WOEID 458191). O usuário clica em "Buscar" na aba Clima para carregar os dados e visualizar a previsão por dia.

Endpoint atual:
```
https://api.hgbrasil.com/weather?woeid=458191
```
Para alterar cidade/local, basta substituir o `woeid` em `ClimaFragment`.

## Arquitetura do app
- Activities
    - `MainActivity`: gerencia Toolbar, TabLayout e ViewPager (abas Clima e Mapa)
    - `SobreActivity`: tela de informações do autor/curso, acessada pelo menu
- Fragments
    - `ClimaFragment`: faz requisição HTTP (OkHttp) e exibe dados com RecyclerView usando `PrevisaoDiaAdapter`
    - `MapaFragment`: placeholder com layout simples para futura integração de mapa
- Adapter
    - `PrevisaoDiaAdapter`: `RecyclerView.Adapter` que renderiza os cards de previsão diária (`previsao_dia_card.xml`)
- Modelos (POJOs)
    - `PrevisaoResposta` → raiz da resposta JSON (campo `results`)
    - `Previsao` → cidade, temperatura atual, umidade e lista `forecast`
    - `PrevisaoDia` → data, dia da semana, máximas/mínimas, descrição e condição
- Utilitários
    - `Utils`: exibe diálogos de erro (AlertDialog) padronizados
- UI e navegação
    - `TabLayout` + `ViewPager` com `FragmentStatePagerAdapter` (`PageAdapterFragment`)
    - Toolbar com menu (`menu_main.xml`) e ação "Sobre"
    - Tema `Theme.AppClima` baseado em `MaterialComponents.DayNight.NoActionBar`

## Requisitos
- Android Studio (Flamingo/Koala ou superior)
- JDK 11
- Gradle Wrapper incluído no projeto
- Min SDK 24, Target SDK 36

## Configuração e execução
1. Clone/extraia o projeto e abra no Android Studio.
2. Sincronize o Gradle (Sync Now).
3. Execute em um dispositivo ou emulador.


## Estrutura de diretórios
```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/appclima/
│   ├── MainActivity.java
│   ├── SobreActivity.java
│   ├── ClimaFragment.java
│   ├── MapaFragment.java
│   ├── PageAdapterFragment.java
│   ├── adapter/PrevisaoDiaAdapter.java
│   ├── modelo/
│   │   ├── PrevisaoResposta.java
│   │   ├── Previsao.java
│   │   └── PrevisaoDia.java
│   └── utils/Utils.java
└── res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── activity_sobre.xml
    │   ├── fragment_clima.xml
    │   ├── fragment_mapa.xml
    │   └── previsao_dia_card.xml
    ├── menu/menu_main.xml
    ├── values/{strings.xml, colors.xml, themes.xml}
    └── ... mipmap, drawable
```

## Fluxos principais
- Clima
    - Botão "Buscar" dispara `AsyncTask` (`CarregarPrevisao`) que chama a API com OkHttp
    - A resposta JSON é parseada com Gson em `PrevisaoResposta`
    - UI atualiza cidade, temperatura, umidade e a lista `forecast` no `RecyclerView`
- Mapa
    - Placeholder; apenas exibe "Mapa"
- Sobre
    - Tela com toolbar e conteúdo estático (nome, RA, curso); ícone de voltar na toolbar

## Dependências
- AndroidX: AppCompat, ConstraintLayout, Activity, CardView
- Material Components
- OkHttp 5.3.0
- Gson 2.13.2

Desenvolvido por Adriana C H Antunes :)