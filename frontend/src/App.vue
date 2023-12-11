<script>
import { RouterView } from 'vue-router';
import NavbarComponent from '@/components/NavbarComponent.vue';


export default {
    name: 'App',
    components: {
        RouterView,
        NavbarComponent,
    },
};
</script>

<template>
    <navbar-component />
    <router-view />
</template>

