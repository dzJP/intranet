<template>
    <div>
        <button @click="openForm">Create News</button>
        <div class="news-form" v-if="showForm">
            <h2>Create News</h2>

            <label for="subject">Subject:</label>
            <input v-model="news.subject" type="text" id="subject" />

            <label for="message">Message:</label>
            <textarea v-model="news.message" id="message"></textarea>

            <label for="date">Date:</label>
            <input v-model="news.date" type="date" id="date" />

            <label for="deadline">Deadline:</label>
            <input v-model="news.deadline" type="date" id="deadline" />

            <button @click="submitForm">Submit</button>
        </div>
    </div>
</template>

<script>
import { useAuthStore } from "@/stores/auth";

export default {
    data() {
        return {
            showForm: false,
            news: {
                subject: '',
                message: '',
                date: '',
                deadline: '',
            },
        };
    },
    methods: {
        openForm() {
            this.showForm = true;
        },

        async submitForm() {
            const auth = useAuthStore();
            const token = auth.token;

            // Define common fetch options
            const fetchOptions = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(this.news),
            };

            try {
                const response = await fetch('http://localhost:8080/api/v1/create-news', fetchOptions);

                if (response.ok) {
                    // Check if the response is JSON
                    if (response.headers.get('content-type')?.includes('application/json')) {
                        try {
                            // Parse JSON response
                            const createdNews = await response.json();
                            this.$emit('newsCreated', createdNews);
                        } catch (jsonError) {
                            console.error('Error parsing JSON response:', jsonError);
                        }
                    } else {
                        // Log non-JSON response
                        console.warn('Received non-JSON response:', response);
                    }

                    this.closeForm();
                } else {
                    // Handle non-OK response
                    const errorText = await response.text();
                    console.error('Error creating news article:', response.status, response.statusText, errorText);
                }
            } catch (error) {
                // Log any general error during form submission
                console.error('Error creating news article:', error);
            }
        },

        closeForm() {
            this.showForm = false;
            this.news = {
                subject: '',
                message: '',
                date: '',
                deadline: '',
            };
        },
    },
};
</script>

<style scoped></style>
