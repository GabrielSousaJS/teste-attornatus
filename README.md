# Teste para desenvolvedor - Attornatus
API Rest desenvolvidas utilizando Spring Boot.

A aplicação contém autorização e autenticação em nível de serviço, além de testes para validar as funcionalidades da mesma.

Existem dois tipos de usuário, ADMIN e CLIENT.

O usuário ADMIN possui acesso a todos os endpoints, sem nenhuma restrição. Mas é necessário cumprir alguns requisitos para que as requisições retornem as respostas
corretas. Como informar um ID válido ao buscar o usuário, preenchar os campos corretamente ao adicionar uma nova pessoa ou endereço.

O usuário CLIENT possui acesso aos endpoints que se referem ao mesmo, ou seja, não é possível que o CLIENT acesse a lista com todos os usuários. Ao pesquisar um usuário,
esse usuário só obterá uma resposta positiva ao informar o próprio ID.

Todas as funcionalidades possuem testes para comprovar que a aplicação está retornando as respostas desejadas para cada endpoint.
