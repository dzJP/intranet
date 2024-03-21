<template>
    <div>
        <button @click="togglePopup" class="invite-btn btn btn-primary">Invite Users</button>
        <div v-if="isPopupVisible" class="invite-popup">
            <div class="popup-content">
                <i class="invite-toggle bi bi-x" @click="togglePopup"></i>
                <h1>Invite Users</h1>
                <form @submit.prevent="inviteUsers">
                    <div v-for="(email, index) in emails" :key="index">
                        <input v-model="emails[index]" type="email" placeholder="email" />
                    </div>
                    <button @click.prevent="addEmail" class="add-btn btn btn-primary">Add</button>
                    <button @click.prevent="removeLastEmail" v-if="emails.length > 1" class="remove-btn btn btn-primary">Remove</button>
                    <button type="submit" class="send-btn btn btn-primary">Send</button>
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

    <style scoped>

    .invite-btn {
        margin: 20px 0 10px 310px;
    }

    .invite-popup {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 400px;
        background: var(--blue);
        color: var(--white);
        padding: 15px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border-radius: 9px;
        z-index: 1;
    }

    .invite-popup h1 {
        font-size: 26px;
        margin-bottom: 10px;
    }

    .invite-popup input {
        width: 100%;
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 5px;
        outline: none;
    }

    .invite-toggle  {
    position: fixed;
    top: 0;
    right: 0;
    height: 35px;
    width: 40px;
    font-size: 25px;
    cursor: pointer;
    transition: 0.3s;
    text-align: center
    }

    .invite-toggle:hover {
    background-color: var(--red);
    color:var(--white);
    border-top-right-radius: 9px;
    cursor: pointer;
    }

    .invite-popup button {
        margin-top: 5px;
        margin-right: 5px;
    }

    .add-btn {
        padding: 3px;
        width: 50px;
        background-color: #223266;
        border-color: #223266;
    }

    .remove-btn {
        padding: 3px;
        width: 80px;
        background-color: #223266;
        border-color: #223266;
    }

    .send-btn {
        position: fixed;
        margin: 0;
        right: 10px;
        width: 70px;
        padding: 3px;
        background-color: #223266;
        border-color: #223266;
    }
    </style>
