<template>
  <button @click="togglePopup" class="viewproject-btn btn btn-primary">View Projects</button>

  <div v-if="isPopupVisible" class="project-popup">
    <i class="project-toggle bi bi-x" @click="togglePopup"></i>
    <div class="popup-content">
      <h1>Projects</h1>

      <ul class="project-list">
        <li v-for="project in projects" :key="project.id">
          <div class="project-info">
          <span>{{ project.project }}</span>
          <div class="button-container">
          <button @click="() => showUpdateForm(project)" class="edit-btn btn btn-edit">Edit</button>
          <button v-if="project.active" @click="() => inactivateProject(project.id)" class="inactivate-btn btn btn-edit">Inactivate</button>
          <button v-else @click="() => activeProjects(project.id)" class="activate-btn btn btn-primary">Activate</button>
          <button @click="() => deleteProject(project.id)" class="delete-btn btn btn-delete">Delete</button>
        </div>
      </div>
        </li>
      </ul>

      <div>
        <button @click="showCreateForm" class="create-btn btn btn-primary">New Project</button>
      </div>

      <form v-if="isCreateFormVisible" @submit.prevent="createNewProject">
          <label for="newProjectName">Project Name:</label>
          <input v-model="newProjectName" type="text" id="newProjectName" required />
          <button type="submit" class="createProject-btn btn btn-primary">Create</button>
          <button @click="hideCreateForm" type="button" class="cancel-btn btn btn-secondary">Cancel</button>
        </form>

        <form v-if="isUpdateFormVisible" @submit.prevent="updateExistingProject">
          <label for="updatedProjectName">New project name:</label>
          <input v-model="updatedProjectName" type="text" id="updatedProjectName" required />
          <button type="submit" class="update-btn btn btn-primary">Update</button>
          <button @click="hideUpdateForm" type="button" class="cancel-btn btn btn-secondary">Cancel</button>
        </form>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useProjectStore } from '../stores/project';

const { getAllProjects, createProject, updateProject } = useProjectStore();

const projects = ref([]);
const newProjectName = ref('');
const isPopupVisible = ref(false);
const isCreateFormVisible = ref(false);
const isUpdateFormVisible = ref(false);
const updatedProjectName = ref('');
let updatingProjectId = ref(null);


const togglePopup = () => {
  isPopupVisible.value = !isPopupVisible.value;
};

const createNewProject = async () => {
  try {
    await createProject({ project: newProjectName.value });
    hideCreateForm();
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error creating project:', error);
  }
};

const showCreateForm = () => {
  isCreateFormVisible.value = true;
};

const hideCreateForm = () => {
  isCreateFormVisible.value = false;
  newProjectName.value = '';
};

const showUpdateForm = (project) => {
  isUpdateFormVisible.value = true;
  updatedProjectName.value = project.project;
  updatingProjectId = project.id;
};

const hideUpdateForm = () => {
  isUpdateFormVisible.value = false;
  updatedProjectName.value = '';
  updatingProjectId = null;
};


const updateExistingProject = async () => {
  try {
    const projectDTO = {
      id: updatingProjectId,
      project: updatedProjectName.value,
    };

    await updateProject(projectDTO);
    hideUpdateForm();
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error updating project:', error);
  }
};

const inactivateProject = async (id) => {
  try {
    await useProjectStore().inactivateProject(id);
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error inactivating project:', error);
  }
};

const activeProjects = async (id) => {
  try {
    await useProjectStore().activateProject(id);
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error activating project', error);
  }
}

const deleteProject = async (id) => {
  try {
    await useProjectStore().deleteProject(id);
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error deleting project:', error);
  }
};

onMounted(async () => {
  try {
    projects.value = await getAllProjects();
  } catch (error) {
    console.error('Error fetching projects:', error);
  }
});

</script>

<style scoped>

.viewproject-btn {
  margin-top: 50px;
  margin-left: 310px;
}

.project-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  font-size: 16px;
  min-height: 300px;
  background: var(--blue);
  color: var(--white);
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 9px;
  z-index: 1;
}

.project-popup h1 {
  font-size: 26px;
  margin-bottom: 20px;
}

.project-popup input {
  width: 40%;
  margin-bottom: 5px;
  margin-left: 10px;
  padding: 3px;
  border-radius: 5px;
  outline: none;
}

.project-popup button {
  padding: 2px 10px;
}

.project-popup button:hover {
  border: 1px solid var(--orange);
}

.createProject-btn, .cancel-btn, .update-btn {
  margin-left: 10px;
  background-color: #223266;
  border-color: #223266;
}

.create-btn {
  background-color: #223266;
  border-color: #223266;
  position: fixed;
  bottom: 20px;
  right: 20px;
}

.project-list {
  list-style: none;
  padding: 0;
}

.project-list li {
  margin: 10px 0;
}

.project-info {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.button-container {
  position: fixed;
  right: 20px;
}

.project-list button {
  margin-left: 10px;
}

.edit-btn {
  background-color: #223266;
  color: var(--white)
}

.inactivate-btn, .activate-btn {
  background-color: #223266;
  color: var(--white)
}

.delete-btn {
  background-color: #223266;
  color: var(--white)
}

.project-toggle  {
    position: fixed;
    top: 0;
    right: 0;
    height: 35px;
    width: 40px;
    font-size: 25px;
    cursor: pointer;
    transition: 0.3s;
    text-align: center
    }

    .project-toggle:hover {
    background-color: var(--red);
    color:var(--white);
    border-top-right-radius: 9px;
    cursor: pointer;
    }

</style>