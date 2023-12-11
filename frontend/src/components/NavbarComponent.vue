<template>
    <div>
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
                        <a href="#" @click="openNewsPopup">News</a> |
                    </li>
                    <li class="nav-item">
                        <a href="#" @click="logout">Logout</a>
                    </li>
                </ul>
            </div>
        </nav>

        <!-- Include the NewsPopup component -->
        <NewsPopup ref="newsPopup" />
    </div>
</template>

<script>
import { useAuthStore } from "@/stores/auth";
import NewsPopup from "@/components/NewsPopup.vue";

export default {
    // eslint-disable-next-line vue/match-component-file-name
    name: 'NavbarComponent', // Intentionally using a single-word name
    components: {
        NewsPopup,
    },
    methods: {
        openNewsPopup() {
            // Call the openPopup method in the NewsPopup component
            this.$refs.newsPopup.openPopup();
        },
        logout() {
            const auth = useAuthStore();
            auth.logout();
        },
    },
};
</script>
