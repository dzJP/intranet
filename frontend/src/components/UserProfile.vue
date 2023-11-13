<template>
    <div class="col-lg-8">
        <div class="card-body">
            <div class="col-lg-8">
                <div class="card-body">
                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Förnamn</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" :value="user.firstName" disabled>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Efternamn</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" :value="user.lastName" disabled>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Email</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" :value="user.email" disabled>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-sm-3">
                            <h6 class="mb-0">Telefonnummer</h6>
                        </div>
                        <div class="col-sm-9 text-secondary">
                            <input type="text" class="form-control" v-model="editedPhoneNumber">
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-3"></div>
                        <div class="col-sm-9 text-secondary">
                            <input type="button" class="btn btn-primary px-4" @click="updatePhoneNumber"
                                value="Spara ändringar">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/user';

export default {
    setup() {
        const userStore = useUserStore();
        const editedPhoneNumber = ref(userStore.phoneNumber);

        // Fetch user profile data from API
        const getUserProfile = async () => {
            try {
                await userStore.getUserDetails();
                editedPhoneNumber.value = userStore.phoneNumber;
            } catch (error) {
                console.error('Error fetching user profile:', error);
            }
        };

        // Update phone number
        const updatePhoneNumber = async () => {
            try {
                await userStore.updatePhoneNumber(editedPhoneNumber.value);
            } catch (error) {
                console.error('Error updating phone number:', error);
            }
        };

        onMounted(() => {
            getUserProfile();
        });

        return {
            user: userStore,
            editedPhoneNumber,
            updatePhoneNumber,
        };
    },
};
</script>