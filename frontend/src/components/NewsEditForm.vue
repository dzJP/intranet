<template>
    <div class="edit-window">
        <p>Form is rendering</p>
        <form @submit.prevent="submitForm">
            <label>Subject</label>
            <input v-model="editedNews.subject" type="text" required>

            <label>Message</label>
            <textarea v-model="editedNews.message" required></textarea>

            <button type="submit" class="btn btn-primary">Update News</button>
            <button @click="cancelEdit" class="btn btn-secondary">Cancel</button>
        </form>
        <!-- console.log -->
        <div>{{ editedNews }}</div>
    </div>
</template>

<script>
export default {
    props: {
        editingNews: Object,
        isEditing: Boolean,
    },
    data() {
        return {
            editedNews: { ...this.editingNews },
        };
    },
    watch: {
        editingNews: {
            handler(newVal) {
                // Update editedNews when editingNews prop changes
                this.editedNews = { ...newVal };
            },
            deep: true,
        },
    },
    methods: {
        submitForm() {
            // Emit the updated news data
            console.log('Submitting form with updated news:', this.editedNews);
            this.$emit('update-news', { ...this.editedNews });
        },
        cancelEdit() {
            console.log('Cancelling edit');
            this.$emit('cancel-edit');
        },
    },
};
</script>