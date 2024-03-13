<template>
    <div class="user-profile">
        <h2>User Profile Page</h2>

        <div v-if="user" class="profile-details">
            <div>
    <router-link to="/admin">Go back</router-link>
                <h6 class="mb-0">First Name</h6>
                <p class="text-secondary">{{ user.firstName }}</p>
            </div>
            <div>
                <h6 class="mb-0">Last Name</h6>
                <p class="text-secondary">{{ user.lastName }}</p>
            </div>
            <div>
                <h6 class="mb-0">Birthdate</h6>
                <p class="text-secondary">{{ user.birthDate }}</p>
            </div>
            <div>
                <h6 class="mb-0">Email</h6>
                <p class="text-secondary">{{ user.email }}</p>
            </div>
            <div>
                <h6 class="mb-0">Phone Number</h6>
                <p class="text-secondary">{{ user.phoneNumber }}</p>
            </div>
        </div>
    </div>
</template>

<script>
import {  ref, onMounted } from 'vue';
import { fetchUserProfile } from '@/stores/user';
import { useRoute } from 'vue-router';

export default {
    setup() {
        const userDetails = ref(null);
        const route = useRoute();

        const fetchUserProfileData = async (email) => {
            try {
                const userData = await fetchUserProfile(email);
                userDetails.value = userData;
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        };

        onMounted(() => {
            const email = route.params.email;
            fetchUserProfileData(email);
        });

        return {
            user: userDetails,
        };
    },
};
</script>

<style scoped>
.user-profile {
    max-width: 600px;
    margin: auto;
}

.profile-details {
    background-color: #f4f4f4;
    padding: 20px;
    border-radius: 8px;
    margin-top: 20px;
}
</style>
