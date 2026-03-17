# Club-Manager-BR

📱 Clubes Brasileiros

Aplicativo Android desenvolvido para gerenciamento e visualização de clubes de futebol brasileiros e suas competições.

🚀 Sobre o Projeto

O Clubes Brasileiros é um aplicativo mobile que permite cadastrar, listar e visualizar informações sobre clubes e competições do futebol brasileiro.

O projeto foi desenvolvido com foco em:

Prática de desenvolvimento Android nativo

Persistência de dados local

Organização em camadas (UI, modelo e persistência)

🛠️ Tecnologias Utilizadas

Java

Android SDK

Room (SQLite) – Persistência de dados

Gradle (Kotlin DSL) – Gerenciamento de build

📌 Funcionalidades

✅ Cadastro de clubes

✅ Listagem de clubes

✅ Visualização de detalhes de um clube

✅ Cadastro de competições

✅ Listagem de competições

✅ Persistência de dados local com Room

✅ Migrações de banco de dados

🏗️ Estrutura do Projeto

O projeto segue uma organização em camadas:

📦 app
 ┣ 📂 modelo
 ┃ ┣ Clube.java
 ┃ ┣ Competicoes.java
 ┃ ┗ Divisao.java
 ┣ 📂 persistencia
 ┃ ┣ ClubeDao.java
 ┃ ┣ CompeticoesDao.java
 ┃ ┣ ClubesDatabase.java
 ┃ ┗ Migrações
 ┣ 📂 utils
 ┣ 📂 activities
 ┃ ┣ ClubeActivity.java
 ┃ ┣ ClubesActivity.java
 ┃ ┣ CompeticoesActivity.java
 ┃ ┗ SobreActivity.java
💾 Banco de Dados

O app utiliza Room para persistência local, com:

DAOs para acesso aos dados

Entidades bem definidas

Conversores para tipos como LocalDate e LocalDateTime

Sistema de migração de versões do banco
