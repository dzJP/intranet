<template>
    <div class="forgot-password-form">
        <h4>{{ headingText}}</h4>
        <form @submit.prevent="onSubmit">
            <div class="form-group mb-3">
                <div class="reset-msg" v-if="passwordSent">
                    <p style="font-family: 'Oxanium', sans-serif;">An email containing your new password has been sent to the provided email address. Please check your inbox and spam/junk folder.</p>
                </div>

                <div class="reset-msg" v-if="!passwordSent">
                    <p style="font-family: 'Oxanium', sans-serif;">Please enter the email address associated with your account. <br>We'll send you a new password shortly.</p>
                </div>
                
                <input type="email" v-if="!passwordSent" class="form-control" id="email" name="email" v-model="email"  placeholder="email"/>
            </div>
            <button type="submit" v-if="!passwordSent" class="resetPassword-btn btn btn-primary">Reset password</button>
        </form>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'ForgotPassword',
    data() {
        return {
            email: '',
            passwordSent: false,
            headingText: 'Reset your password',
        };
    },
    methods: {
        onSubmit() {
            const formData = new FormData();
            formData.append('email', this.email);

            axios.post('http://localhost:8080/api/v1/reset-password', formData)
                .then(() => {
                    this.passwordSent = true;
                    this.headingText = 'Password successfully sent';
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        },
    },
};
</script>

<style scoped>

.reset-msg {
    margin-top: 10px;
    margin-bottom: 20px;
    font-size: 16px;
}

.resetPassword-btn {
    background-color: var(--light-blue);
    border-color: var(--light-blue);
    margin-top: 5px;
    width: 200px;
    height: 45px;
    font-size: 16px;
    font-family: 'Oxanium', sans-serif;
    text-transform: uppercase;
    letter-spacing: 2px;
}

.form-control::placeholder {
    color: var(--purple-variance);
}
</style>
