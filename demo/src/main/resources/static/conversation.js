document.addEventListener('DOMContentLoaded', function () {
    const messageContainer = document.getElementById("messageContainer");
    const sendMessageBtn = document.getElementById("sendMessageBtn");
    const messageContent = document.getElementById("messageContent");
    const otherUserSpan = document.getElementById("otherUser");
    const userId = window.location.pathname.split("/").pop(); // Pega o ID do outro usuário da URL

    // Carregar conversa
    fetch(`/messages/conversation/${userId}`)
        .then(response => response.json())
        .then(messages => {
            otherUserSpan.textContent = messages.otherUser.username;
            messages.messages.forEach(msg => {
                const messageDiv = document.createElement('div');
                messageDiv.classList.add('message');
                if (msg.isFromCurrentUser) {
                    messageDiv.classList.add('from-current-user');
                }
                messageDiv.textContent = msg.content;
                messageContainer.appendChild(messageDiv);
            });
        });

    sendMessageBtn.addEventListener('click', function () {
        const content = messageContent.value;
        if (content.trim() === "") return;

        // Enviar mensagem para o backend
        fetch(`/messages/send?recipientId=${userId}&content=${content}`, {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                messageContent.value = "";
                alert("Mensagem enviada!");
                window.location.reload(); // Recarregar a conversa
            }
        });
    });
});
