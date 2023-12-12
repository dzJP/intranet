<template>
    <div class="popup" v-if="showPopup">
        <div class="popup-content">
            <button @click="closePopup">Close</button>
            <button @click="navigate(-1)" :disabled="currentIndex === 0">Previous</button>
            <button @click="navigate(1)" :disabled="currentIndex === newsList.length - 1">Next</button>

            <template v-if="loading">
                <p>Loading news...</p>
            </template>

            <template v-else-if="newsList?.length > 0">
                <h3>News</h3>
                <div v-for="(newsItem, index) in newsList" :key="newsItem.id" v-show="currentIndex === index">
                    <h5>{{ newsItem.subject }}</h5>
                    <p>{{ newsItem.message }}</p>
                    <p>Date: {{ formatDate(newsItem.date) }}</p>
                    <p>Deadline: {{ formatDate(newsItem.deadline) }}</p>
                    <hr />
                </div>
            </template>

            <template v-else>
                <p>No news available.</p>
            </template>
        </div>
    </div>
</template>

<script>
import { useAuthStore } from "@/stores/auth";

export default {
    data() {
        return {
            showPopup: false,
            newsList: [],
            currentIndex: 0,
            loading: false,
        };
    },
    methods: {
        async openPopup() {
            try {
                this.loading = true;
                const auth = useAuthStore();
                const token = auth.token;

                const response = await fetch("http://localhost:8080/api/v1/get-news", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (response.ok) {
                    this.newsList = await response.json();
                    this.showPopup = true;
                } else {
                    console.error("Error fetching news articles. Status:", response.status);
                }
            } catch (error) {
                console.error("Error fetching news articles:", error);
            } finally {
                this.loading = false;
            }
        },
        closePopup() {
            this.showPopup = false;
        },

        navigate(offset) {
            this.currentIndex += offset;
        },
        formatDate(date) {
            if (!date) {
                return 'N/A';
            }

            // parse the date
            const parsedDate = new Date(date);

            // Check if the parsed date is valid
            if (parsedDate instanceof Date && !isNaN(parsedDate)) {
                return parsedDate.toLocaleDateString();
            } else {
                return 'N/A';
            }
        },
    },
};
</script>

<style scoped>
.popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
}

.popup-content {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
    text-align: center;
}

button {
    margin: 5px;
}
</style>