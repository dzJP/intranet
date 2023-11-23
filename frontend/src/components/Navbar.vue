<script>
import { useAuthStore } from '@/stores/auth';

export default {
    name: 'nav-bar',
    methods: {
        logout() {
            const auth = useAuthStore();
            auth.logout();
        },
    },
};
</script>

<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <router-link to="/">Home</router-link> |
                </li>
                <li class="nav-item">
                    <router-link to="/profile">Profile</router-link> |
                </li>
                <li class="nav-item">
                    <router-link to="/colleagues">Colleagues</router-link> |
                </li>
                <li class="nav-item">
                    <a href="#" @click="logout">Logout</a>
                </li>
            </ul>
        </div>
    </nav>
</template>
