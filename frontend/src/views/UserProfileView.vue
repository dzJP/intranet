<template>
    <div class="user-profile">
        <h2>User Profile Page</h2>

        <div v-if="userDetails" class="profile-details">
            <div>
                <h6 class="mb-0">First Name</h6>
                <p class="text-secondary">{{ userDetails.firstName }}</p>
            </div>
            <div>
                <h6 class="mb-0">Last Name</h6>
                <p class="text-secondary">{{ userDetails.lastName }}</p>
            </div>
            <div>
                <h6 class="mb-0">Email</h6>
                <p class="text-secondary">{{ userDetails.email }}</p>
            </div>
            <div>
                <h6 class="mb-0">Phone Number</h6>
                <p class="text-secondary">{{ userDetails.phoneNumber }}</p>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

export default {
    setup() {
        const userDetails = ref(null);
        const route = useRoute();

        const fetchUserProfile = async (email) => {
            try {
                const response = await axios.get(`http://localhost:8080/api/v1/user/${email}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                console.log('User details response:', response.data);

                userDetails.value = response.data;
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        };

        onMounted(() => {
            // Extract the username from the route parameters
            const email = route.params.email;
            fetchUserProfile(email);
        });

        return {
            userDetails,
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

h2 {
    color: #333;
}
</style>
