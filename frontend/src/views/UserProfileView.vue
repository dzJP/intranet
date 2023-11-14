<template>
    <div>
        <h2>User Profile View page</h2>
        <UserProfileDetails :username="route.params.username" />
    </div>
</template>

<script>
import UserProfileDetails from '@/components/UserProfileDetails.vue';
import { useRoute } from 'vue-router';

export default {
    components: {
        UserProfileDetails,
    },
    setup() {
        const route = useRoute();
        return {
            route,
        };
    },
};
</script>