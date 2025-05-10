Como Rodar a Aplicação

    Pré-requisitos:

        Java 17 ou superior: A aplicação foi desenvolvida utilizando Spring Boot, que requer Java 17 ou uma versão superior.

        Banco de Dados: O projeto usa o Spring Data JPA, que é compatível com diversos bancos de dados. No exemplo, você pode usar o H2 (para testes locais), ou MySQL/PostgreSQL (para ambientes de produção).

        Maven/Gradle: O gerenciamento de dependências é feito via Maven.

    Passos para rodar:

        Clonando o repositório:
        Se você ainda não tem o código, faça o clone do repositório para a sua máquina.

git clone <URL do repositório>
cd <diretório do projeto>

Configuração do Banco de Dados:
Para o H2 (configuração padrão para testes), a aplicação vai gerar automaticamente o banco de dados em memória. Para um banco de dados como MySQL ou PostgreSQL, altere as configurações de banco de dados no arquivo application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update

Instalar Dependências:
Se você estiver usando Maven, rode o comando para instalar as dependências:

mvn clean install

Ou com Gradle:

gradle build

Rodar a Aplicação:
Se a aplicação for construída com Maven, basta rodar:

mvn spring-boot:run

Ou, se você tiver o arquivo .jar, pode rodar com:

    java -jar target/secure-chat-1.0.0.jar

Acessando a Aplicação:
Após a aplicação ser inicializada, você pode acessá-la no navegador via o endereço:

    http://localhost:8080

    Aqui, você será redirecionado para a página de login. Use a funcionalidade de registro para criar um usuário novo, ou faça login com um usuário já existente.

Arquitetura e Decisões de Desenvolvimento

    Spring Boot:

        Escolha: Utilizamos o Spring Boot para criar uma aplicação Java autônoma, que configura automaticamente diversos componentes e simplifica o processo de desenvolvimento. Ele também suporta a criação de APIs RESTful, o que é fundamental para a troca de mensagens entre usuários.

        Vantagens: Facilita a inicialização do projeto, sem a necessidade de configurações complexas. Ele também oferece uma infraestrutura robusta para trabalhar com segurança, persistência de dados e injeção de dependência.

    Spring Security:

        Escolha: Usamos Spring Security para garantir a autenticação e autorização de usuários. Ele protege a aplicação, garantindo que apenas usuários autenticados possam acessar mensagens ou enviar novas.

        Vantagens: Ele permite configurar facilmente login, logout, controle de permissões e proteção contra ataques, como CSRF.

    JPA (Java Persistence API):

        Escolha: A persistência de dados é feita usando JPA com o Spring Data JPA para salvar usuários e mensagens no banco de dados.

        Vantagens: O Spring Data JPA permite criar repositórios de forma rápida e intuitiva, sem a necessidade de escrever SQL manualmente. Ele também fornece funcionalidades como ordenação e paginação de resultados de forma automática.

    Criptografia (AES):

        Escolha: Para garantir a privacidade das mensagens, usamos criptografia AES com um vetor de inicialização (IV) único por mensagem. As mensagens são criptografadas antes de serem armazenadas no banco de dados e apenas o destinatário da mensagem pode descriptografá-la.

        Vantagens: A criptografia AES é um dos métodos mais seguros para proteger dados sensíveis. Com isso, mesmo que alguém consiga acessar o banco de dados, as mensagens estarão seguras e ilegíveis sem a chave de criptografia.

    API RESTful e Frontend:

        Backend (API RESTful): A comunicação entre o frontend e o backend é realizada via API RESTful. As requisições HTTP são manipuladas pelo controlador MessageController, que processa as mensagens enviadas, a obtenção das conversas, e a exibição das mensagens de maneira criptografada.

        Frontend (HTML, CSS, JavaScript): A interface do usuário é construída com HTML, CSS e JavaScript. Utilizamos AJAX para realizar chamadas assíncronas à API de mensagens, permitindo que as conversas sejam atualizadas sem a necessidade de recarregar a página inteira. A interface permite que o usuário veja uma lista de contatos e converse de forma segura com outros usuários.

    Design Responsivo:

        A aplicação foi projetada para ser responsiva, permitindo que a interface se ajuste automaticamente a diferentes tamanhos de tela (desktop, tablet, celular). O CSS foi usado para garantir que a aplicação tenha uma aparência limpa e fácil de usar em qualquer dispositivo.

Fluxo de Funcionalidade:

    Cadastro e Login:

        O usuário pode se registrar e fazer login. O Spring Security gerencia a autenticação e a autorização para garantir que o usuário só tenha acesso às funcionalidades que lhe são permitidas.

    Troca de Mensagens:

        O usuário pode enviar mensagens criptografadas para outros usuários. As mensagens são armazenadas no banco de dados de forma segura, com o conteúdo criptografado. Apenas o destinatário da mensagem pode descriptografá-la usando a chave de criptografia armazenada no perfil do usuário.

    Exibição de Conversas:

        Ao acessar uma conversa com outro usuário, a aplicação recupera todas as mensagens trocadas entre os dois, ordenadas por data e hora. A mensagem é descriptografada antes de ser exibida para o usuário.

    Recent Contacts:

        A lista de contatos recentes exibe os usuários com os quais o usuário teve uma troca de mensagens recentemente. Essa funcionalidade é feita através de consultas no banco de dados, retornando os contatos com os quais o usuário interagiu.

Segurança:

    Autenticação e Autorização:

        O Spring Security é usado para garantir que apenas usuários autenticados possam acessar as mensagens. Ele também protege contra ataques como CSRF e fornece uma configuração personalizada para login e logout.

    Criptografia de Mensagens:

        A criptografia das mensagens assegura que, mesmo que um atacante tenha acesso ao banco de dados, não será capaz de ler o conteúdo das mensagens sem a chave de criptografia.

    Proteção de Dados Sensíveis:

        As senhas dos usuários são armazenadas de forma segura usando o BCryptPasswordEncoder. Isso garante que as senhas não sejam armazenadas em texto simples.

Com essas escolhas e abordagens, garantimos que a aplicação seja segura, escalável, e fácil de usar, atendendo aos requisitos de privacidade e segurança das mensagens trocadas entre os usuários.
