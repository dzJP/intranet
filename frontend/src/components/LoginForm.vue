<template>
		<img src="@/assets/logo.png" alt="Deltma logo" class="logo" />
	<div class="container">
		<div>
		<h1>Deltma</h1>
		<h2>Intranät</h2>
		</div>
		<div class="login-form">
			<form @submit.prevent="onSubmit">
				<h4>Sign in</h4>
				<div class="form-group mb-3">
					<input type="email" class="form-control" id="email" v-model="user.email" placeholder="email" />
				</div>
				<div class="form-group mb-3">
					<input type="password" class="form-control" id="password" v-model="user.password" placeholder="password" />
					<a @click="showForgotPasswordPopup" class="forgotpassword">Forgot password?</a>
				</div>
				<div class="pt-1 mb-4">
					<button type="submit" class="signin-btn btn btn-primary">Sign in</button>
				</div>
			</form>
		</div>

		<transition name="fade">
			<div v-if="showPopup" class="popup">
				<i class="forgetpassword-toggle bi bi-x" @click="closePopup"></i>
				<ForgotPassword />
			</div>
		</transition>
	</div>
</template>

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
			password: 'password',
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

<style scoped>

.logo {
	width: 150px;
	height: 50px;
	margin-top: 80px;
	margin-left: 60px;
}

.container {
	display: flex;
	flex-direction: row;
	justify-content: center;
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
}

.container h1 {
	margin-top: 25px;
	color: var(--orange);
}

.container h2 {
	color: var(--white);
}

.container h1, .container h2 {
	font-size: 52px;
	text-transform: uppercase;
	font-weight: 700;
}

.login-form {
	margin-left: 150px;
	width: 350px;
	color: var(--white);
}

.login-form input {
	width: 100%;
	height: 40px;
	border-radius: 5px;
	margin: 5px 0;
}

.login-form a {
	color: var(--white);
	text-decoration: none;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
}

.signin-btn {
	width: 100%;
	margin: auto;
	font-size: 20px;
	letter-spacing: 1.5px;
}

.login-form a:hover {
	text-decoration: underline;
}

.popup {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background: var(--blue);
	color: var(--white);
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	max-width: 500px;
	border-radius: 5px;
}

.forgetpassword-toggle  {
    position: absolute;
    top: 0;
    right: 0;
    height: 35px;
    width: 40px;
	font-size: 25px;
    color: var(--white);
    cursor: pointer;
    transition: 0.3s;
	text-align: center;
    }

    .forgetpassword-toggle:hover {
    background-color: var(--red);
	border-top-right-radius: 5px;
    color: var(--white);
    cursor: pointer;
    }

</style>