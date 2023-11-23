<template>
    <div class="forgot-password-form">
        <h4>Glömt lösenord</h4>
        <form @submit.prevent="onSubmit">
            <div class="form-group mb-3">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" v-model="email" />
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
            const formData = new FormData();
            formData.append('email', this.email);

            axios.post('http://localhost:8080/api/v1/reset-password', formData)
                .then(() => {
                })
                // .catch(error => {
                // });
        },
    },
};
</script>

<style></style>
