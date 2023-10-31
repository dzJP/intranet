<template>
  <div class="forgot-password-form">
    <h4>Glömt lösenord</h4>
    <form @submit.prevent="onSubmit">
      <div class="form-group mb-3">
        <label for="email">Email</label>
        <input type="email" class="form-control" id="email" v-model="email" />
      </div>
      <button type="submit" class="btn btn-primary">Återställ lösenord</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'ForgotPassword',
  data() {
    return {
      email: '',
    };
  },
  methods: {
    onSubmit() {
      console.log('Sending reset password link to:', this.email);
    },
  },
};
</script>

