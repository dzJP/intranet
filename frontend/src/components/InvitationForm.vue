<template>
    <div>
        <button @click="togglePopup" class="invite-btn">Invite users</button>
        <div v-if="isPopupVisible" class="popup">
            <div class="popup-content">
                <i class="toggle bi bi-x" @click="togglePopup"></i>
                <h2>Invite Users</h2>
                <form @submit.prevent="inviteUsers">
                    <div v-for="(email, index) in emails" :key="index">
                        <input v-model="emails[index]" type="email" placeholder="Enter email.."/>
                    </div>
                    <div class="button-container">
                        <button @click.prevent="addEmail" class="add-email-button">Add Another Email</button>
                        <button @click.prevent="removeLastEmail" v-if="emails.length > 1" class="remove-email-button">Remove Last Email</button>
                        <button type="submit" class="submit-button">Send Invitations</button>
                    </div>
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
    color: var(--white);
    padding: 10px 22px;
    font-size: 16px;
    font-weight: 600;
    font-family: 'Oxanium', sans-serif;
    border: 1px solid var(--orange);
    border-radius: 5px;
    transition: background-color 0.3s ease, color 0.3s ease;
    background-color: #040B24;
    margin-left: 310px;
    margin-top: 20px;
}

.invite-btn:hover {
    background-color: var(--orange);
    color: var(--white);
}

.popup {
    position: absolute;
    top: 50%;
    left: 50%;
    border: 2px solid #111C44;
    transform: translate(-50%, -50%);
    background-color: var(--dark-blue);
    padding: 20px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    z-index: 1;
}

.popup-content {
    text-align: center;
}

.add-email-button,
.remove-email-button,
.submit-button {
    background-color: var(--light-blue);
    font-size: 16px;
}

input[type="email"] {
    padding: 10px;
    width: 100%;
    height: 40px;
    border: 1px solid #ccc;
    border-radius: 5px;
    margin-bottom: 15px;
    font-family: 'Oxanium', sans-serif;
    font-size: 16px;
    color: #333;
}


h2 {
    color: #ffffff;
    font-size: 22px;
    font-weight: 600;
    font-family: 'Oxanium', sans-serif;
    text-transform: uppercase;
}

button {
    margin-left: 10px;
    margin-bottom: 10px;
    padding: 10px;
    background-color: var(--light-blue);
    font-family: 'Oxanium', sans-serif;
    font-weight: 600;
    cursor: pointer;
    color: #fff;
    border: none;
    border-radius: 5px;
    text-transform: uppercase;
}

input[type="email"] {
    padding: 10px;
    width: 200px;
    border: 1px solid #ccc;
    border-radius: 3px;
}

.toggle {
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