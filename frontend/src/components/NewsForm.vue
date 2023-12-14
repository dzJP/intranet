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
import { createNews } from "@/stores/news";

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
            try {
                const createdNews = await createNews(this.news);
                this.$emit('newsCreated', createdNews);
                this.closeForm();
            } catch (error) {
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
