<template>
    <div>
        <button @click="togglePopup" class="invite-btn btn btn-primary">Invite Users</button>
        <div v-if="isPopupVisible" class="popup">
            <div class="popup-content">
                <i class="toggle bi bi-x" @click="togglePopup"></i>
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
import { sendInvitations } from '../stores/invitation';

export default {
    name: 'InvitationForm',
    data() {
        return {
            isPopupVisible: false,
            emails: [''], // Initial array with empty strings for 1 field (or more)
        };
    },
    methods: {
        togglePopup() {
            this.isPopupVisible = !this.isPopupVisible;
        },
        async inviteUsers() {
            const validEmails = this.emails.filter(email => email.trim() !== '');
            console.log('Valid emails:', validEmails);

            if (validEmails.length > 0) {
                try {
                    await sendInvitations(validEmails, this.uuid);
                    this.togglePopup();
                } catch (error) {
                    console.error('Error sending invitations:', error);
                }
            } else {
                console.error('No valid emails to send invitations.');
            }
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

    .invite-btn {
        margin-left: 310px;
        margin-top: 20px;
    }
    .popup {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: white;
        padding: 20px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        z-index: 1;
    }

    .popup-content {
        text-align: center;
    }

    h2 {
        color: #333;
    }

    button {
        margin-left: 10px;
        margin-bottom: 10px;
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

    .toggle  {
    position: absolute;
    top: 0;
    right: 0;
    height: 35px;
    width: 40px;
    }

    i {
    font-size: 25px;
    color: #454444;
    cursor: pointer;
    transition: 0.3s;
    }

    .toggle:hover {
    background-color: #df3232;
    color: #fff;
    cursor: pointer;
    }

    </style>
