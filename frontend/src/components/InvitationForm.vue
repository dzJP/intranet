<template>
    <div>
        <button @click="togglePopup" class="btn btn-primary">Invite Users</button>
        <div v-if="isPopupVisible" class="popup">
            <div class="popup-content">
                <span class="toggle" @click="togglePopup">&times;</span>
                <h2>Invite Users</h2>
                <form @submit.prevent="inviteUsers">
                    <div v-for="(email, index) in emails" :key="index">
                        <input v-model="emails[index]" type="email" />
                    </div>
                    <button @click.prevent="addEmail">Add Another Email</button>
                    <button @click.prevent="removeLastEmail" v-if="emails.length > 1">Remove Last Email</button>
                    <button type="submit">Send Invitations</button>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'InvitationForm',
    data() {
        return {
            isPopupVisible: false,
            emails: [''], // Initial array with  empty strings for 1 fields (or more)
        };
    },
    methods: {
        togglePopup() {
            this.isPopupVisible = !this.isPopupVisible;
        },
        inviteUsers() {
            const validEmails = this.emails.filter(email => email.trim() !== '');
            console.log('Valid emails:', validEmails);

            if (validEmails.length > 0) {
                this.sendInvitations(validEmails);
            } else {
                console.error('No valid emails to send invitations.');
            }
        },
        sendInvitations(emails) {
            const jwtToken = localStorage.getItem('token');
            console.log('JWT Token:', jwtToken);

            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

            axios
                .post('http://localhost:8080/api/v1/admin/send-invitations', emails, {
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${jwtToken}`
                    }
                })
                .then(response => {
                    console.log(response.data);
                    this.togglePopup();
                })
                .catch(error => {
                    console.error('Error sending invitations:', error);
                });
        },
        addEmail() {
            this.emails.push('');
        },
        removeLastEmail() {
            if (this.emails.length > 1) {
                this.emails.pop();
            }
        },
    },
};
</script>

<style>
.popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: white;
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.popup-content {
    text-align: center;
}

h2 {
    color: #333;
}

button {
    margin: 5px;
    padding: 10px;
    cursor: pointer;
    background-color: #3498db;
    color: #fff;
    border: none;
    border-radius: 3px;
}

input[type="email"] {
    padding: 10px;
    width: 200px;
    border: 1px solid #ccc;
    border-radius: 3px;
}
</style>
