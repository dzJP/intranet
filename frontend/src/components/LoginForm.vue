<script>
import { reactive, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import ForgotPassword from './ForgotPassword.vue';

export default {
	name: 'LoginForm',
	components: {
		ForgotPassword
	},
	setup() {
		const user = reactive({
			email: '',
			password: '',
		});

		function onSubmit() {
			console.log(user);
			if (user.email !== '' && user.password !== '') {
				useAuthStore().login(user.email, user.password);
			}
		}

		const showPopup = ref(false);

		function showForgotPasswordPopup() {
			showPopup.value = true;
		}

		function closePopup() {
			showPopup.value = false;
		}

		return {
			user,
			onSubmit,
			showPopup,
			showForgotPasswordPopup,
			closePopup,
		};
	},
};
</script>
<template>
	<div class="container">
		<div class="login-form mx-auto">
			<form @submit.prevent="onSubmit">
				<div class="form-group mb-3">
					<label for="email">Email</label>
					<input type="email" class="form-control" id="email" v-model="user.email" />
				</div>
				<div class="form-group mb-3">
					<label for="password">Lösenord</label>
					<input type="password" class="form-control" id="password" v-model="user.password" />
				</div>
				<button type="submit" class="btn btn-primary">Logga in</button>
			</form>
		</div>

		<div class="text-center mt-3">
			<button @click="showForgotPasswordPopup" class="btn btn-primary">Glömt lösenord</button>
		</div>

		<transition name="fade">
			<div v-if="showPopup" class="popup">
				<ForgotPassword />
				<button @click="closePopup" class="btn btn-primary">Stäng</button>
			</div>
		</transition>
	</div>
</template>
<style scoped>
.login-form {
	width: 500px;
}

.popup {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background: white;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>