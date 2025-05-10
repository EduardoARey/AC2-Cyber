document.addEventListener('DOMContentLoaded', function () {
    const userList = document.getElementById("userList");
    const sendMessageBtn = document.getElementById("sendMessageBtn");
    const messageContent = document.getElementById("messageContent");

    // Carregar lista de usuários
    fetch('/api/users')
        .then(response => response.json())
        .then(users => {
            users.forEach(user => {
                const li = document.createElement('li');
                li.textContent = user.username;
                li.addEventListener('click', () => {
                    window.location.href = `/messages/conversation/${user.id}`;
                });
                userList.appendChild(li);
            });
        });

    sendMessageBtn.addEventListener('click', function () {
        const content = messageContent.value;
        if (content.trim() === "") return;

        // Enviar mensagem para o backend
        fetch('/messages/send', {
            method: 'POST',
            body: JSON.stringify({ content }),
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => {
            if (response.ok) {
                messageContent.value = "";
                alert("Mensagem enviada!");
            }
        });
    });
});
