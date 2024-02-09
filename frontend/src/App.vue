<script>
import { RouterView } from 'vue-router';
import SideBar from './components/SideBar.vue';


export default {
    name: 'App',
    components: {
        RouterView,
        SideBar,
    },
};
</script>

<template>
    <side-bar />
    <router-view />
</template>

