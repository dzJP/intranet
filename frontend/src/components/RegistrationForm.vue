<template>
    <div class="container">
        <div class="registration-form mx-auto">
            <form @submit.prevent="onSubmit">

                <div class="form-group mb-3">
                    <label for="email">Email</label>
                    <input type="email" class="form-control" id="email" v-model="user.email" />
                </div>

                <div class="form-group mb-3">
                    <label for="firstName">First Name</label>
                    <input type="firstName" class="form-control" id="firstName" v-model="user.firstName" />
                </div>

                <div class="form-group mb-3">
                    <label for="lastName">Last Name</label>
                    <input type="lastName" class="form-control" id="lastName" v-model="user.lastName" />
                </div>

                <div class="form-group mb-3">
                    <label for="birthdate">Birthdate </label>
                    <input type="date" class="form-control" id="birthdate" v-model="user.birthDate" />
                </div>

                <div class="form-group mb-3">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" v-model="user.password" />
                </div>

                <div class="form-group mb-3">
                    <label for="phoneNumber">Phone number</label>
                    <input type="phoneNumber" class="form-control" id="phoneNumber" v-model="user.phoneNumber" />
                </div>

                <button type="submit" class="btn btn-primary">Register</button>
            </form>
        </div>
    </div>
</template>

<script>
import { reactive } from 'vue';
import { useAuthStore } from '@/stores/auth';

const user = reactive({
    email: '',
    password: '',
});

function onSubmit() {
    console.log('onSubmit triggered', user);
    if (user.email !== '' && user.firstName !== '' && user.lastName !== '' && user.password !== '' && user.phoneNumber !== '' && user.birthDate !== '') {
        useAuthStore().register(
            user.email,
            user.firstName,
            user.lastName,
            user.password,
            user.phoneNumber,
            user.birthDate
        );
    }
}

export default {
    name: 'RegistrationForm',
    setup() {
        return {
            user,
            onSubmit,
        };
    },
};
</script>

<style scoped>
.registration-form {
    width: 500px;
}
</style>
