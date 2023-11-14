<template>
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
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
    props: {
        username: String,
    },
    setup(props) {
        const userDetails = ref(null);

        const fetchUserProfile = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/v1/user/${props.username}`, {
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
            fetchUserProfile();
        });

        return {
            userDetails,
        };
    },
};
</script>

<style scoped>
.profile-details {
    background-color: #f4f4f4;
    padding: 20px;
    border-radius: 8px;
    margin-top: 20px;
}
</style>
