<script>
import { RouterView } from 'vue-router';
import SideBar from './components/SideBar.vue';
import { useAuthStore } from './stores/auth';
import { computed } from 'vue';

export default {
    name: 'App',
    components: {
        RouterView,
        SideBar,
    },
    setup() {
        const authStore = useAuthStore();
        const isAuthenticated = computed(() => !!authStore.token);

        return {
            isAuthenticated,
        };
    }
};
</script>

<template>
    <side-bar v-if="isAuthenticated" />
    <router-view />
</template>

