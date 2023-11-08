<template>
    <div class="forgot-password-form">
        <h4>Glömt lösenord</h4>
        <form @submit.prevent="onSubmit">
            <div class="form-group mb-3">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" v-model="email" />
            </div>
            <button type="submit" class="btn btn-primary">Återställ lösenord</button>
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
        };
    },
    methods: {
        onSubmit() {
            axios.post('http://localhost:8080/api/v1/reset-password', {
                email: this.email
            })
                .then(() => {
                    console.log('Password reset link sent successfully');
                })
                .catch(error => {
                    console.error('Error sending password reset link:', error);
                });
        },
    },
};
</script>

<style></style>
