<template>
    <div class="user-profile-container">
        <div class="card-body">
            <div class="column">
                <div class="content">
                    <div class="row mb-3">
                        <div class="label">
                            <h6 class="mb-0">Förnamn</h6>
                        </div>
                        <div class="value text-secondary">
                            <input type="text" class="form-control" v-model="editedFirstName" >
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="label">
                            <h6 class="mb-0">Efternamn</h6>
                        </div>
                        <div class="value text-secondary">
                            <input type="text" class="form-control" v-model="editedLastName" >
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="label">
                            <h6 class="mb-0">Email</h6>
                        </div>
                        <div class="value text-secondary">
                            <input type="text" class="form-control" :value="user.email" disabled>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="label">
                            <h6 class="mb-0">Telefonnummer</h6>
                        </div>
                        <div class="value text-secondary">
                            <input type="text" class="form-control" v-model="editedPhoneNumber">
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="label">
                            <h6 class="mb-0">Profilbild</h6>
                        </div>
                        <div class="value text-secondary img-container">
                            <input type="file" ref="fileInput" @change="handleFileChange" class="file-input" />
                            <div v-if="user.profilePictureUrl">
                                <img :src="user.profilePictureUrl" alt="Profile Picture">
                            </div>
                            <div>
                                <button class="btn-primary px-4" @click="uploadProfilePicture">Ladda upp</button>
                            </div>
                            <div>
                                <button class="btn-primary px-4" @click="deleteProfilePicture">Radera bild</button>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label"></div>
                        <div class="value text-secondary">
                            <input type="button" class="btn-primary px-4" value="Spara ändringar" @click="saveChanges">
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
        const editedFirstName = ref(userStore.firstName);
        const editedLastName = ref(userStore.lastName);
        const fileInput = ref(null);
        const selectedFile = ref(null);

        // Fetch user profile data from API
        const getUserProfile = async () => {
            try {
                await userStore.getUserDetails();
                editedPhoneNumber.value = userStore.phoneNumber;
                editedFirstName.value = userStore.firstName;
                editedLastName.value = userStore.lastName;
            } catch (error) {
                console.error('Error fetching user details:', error.response || error.message);
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
        // Update first name
        const updateFirstName = async () => {
            try {
                await userStore.updateFirstName(editedFirstName.value);
            } catch (error) {
                console.error('Error updating first name:', error);
            }
        };
        // Update last name
        const updateLastName = async () => {
            try {
                await userStore.updateLastName(editedLastName.value);
            } catch(error) {
                console.error('Error updating last name', error);
            }
        };
        // Save changes
        const saveChanges = async () => {
            if (editedPhoneNumber.value !== userStore.phoneNumber) {
                await updatePhoneNumber();
        }
            if (editedFirstName.value !== userStore.firstName) {
                await updateFirstName();
        }
            if (editedLastName.value !== userStore.lastName) {
            await updateLastName();
        }
        };
        const handleFileChange = () => {
            const fileInput = document.querySelector('input[type="file"]');
            const file = fileInput.files[0];

            if (file && file.type.startsWith('image/')) {
                selectedFile.value = file;
            } else {
                console.error('Please select a valid image file.');
            }
        };
        const uploadProfilePicture = async () => {
            if (!selectedFile.value) {
                console.error('Please select a file.');
                return;
            }

            try {
                await userStore.uploadProfilePictureWithSasToken(selectedFile.value);
                window.location.reload();
            } catch (error) {
                console.error('Error uploading profile picture:', error);
            }
        };
        const deleteProfilePicture = async () => {
            try {
                await userStore.deleteProfilePicture();
            } catch (error) {
                console.error('Error deleting profile picture:', error);
            }
        }
        onMounted(() => {
            getUserProfile();
        });
        return {
            user: userStore,
            editedPhoneNumber,
            updatePhoneNumber,
            editedFirstName,
            updateFirstName,
            editedLastName,
            updateLastName,
            handleFileChange,
            uploadProfilePicture,
            deleteProfilePicture,
            fileInput,
            saveChanges,
        };
    },
};
</script>

<style>
.user-profile-container {
    max-width: 800px;
    margin: auto;
}

.card-body {
    background-color: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
}

.content {
    margin-top: 10px;
}

.btn-primary {
    width: 100%;
    max-width: 200px;
    margin-top: 10px;
}

.form-control {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
}


.img-container img {
    max-width: 100%;
    max-height: 200px;
    height: auto;
    border-radius: 50%;
    margin-top: 10px;
}
</style>