<script>
import { RouterView } from 'vue-router';
import NavBar from '@/components/Navbar.vue';


export default {
    name: 'App',
    components: {
        RouterView,
        NavBar,
    },
};
</script>

<template>
    <nav-bar />
    <router-view />
</template>

