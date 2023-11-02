<template>
    <div>
        <button @click="togglePopup" class="btn btn-primary">Invite Users</button>
        <div v-if="isPopupVisible" class="popup">
            <div class="popup-content">
                <span class="toggle" @click="togglePopup">&times;</span>
                <h2>Invite Users</h2>
                <form @submit.prevent="inviteUsers">
                    <input v-for="(email, index) in emails" :key="index" v-model="emails[index]" type="email" required />
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
            emails: [''],
        };
    },
    methods: {
        togglePopup() {
            this.isPopupVisible = !this.isPopupVisible;
        },
        inviteUsers() {
            console.log('inviteUsers method called');
            const validEmails = this.emails.filter(email => email.trim() !== '');
            console.log('Valid emails:', validEmails);

            if (validEmails.length > 0) {
                this.sendInvitations(validEmails);
            }
        },
        sendInvitations(emails) {
            const csrfToken = document.querySelector('meta[name="_csrf"]').content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
            const jwtToken = localStorage.getItem('token');
            console.log('JWT Token:', jwtToken);

            axios
                .post('http://localhost:8080/api/v1/send-invitations', emails, {
                    headers: {
                        [csrfHeader]: csrfToken,
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${jwtToken}`
                    }
                })
                .then(response => {
                    console.log('Invitations sent successfully!', response.data);
                    this.togglePopup();
                })
                .catch(error => {
                    console.error('Error sending invitations:', error);
                });
        }
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
</style>
