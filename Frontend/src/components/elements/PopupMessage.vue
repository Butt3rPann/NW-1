<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  message: {
    type: String,
    required: true,
  },
  isShowPopup: {
    type: Boolean,
    require: true
  }
})

const displayPopup = ref(false)
const fadeOut = ref(false)

function closePopup() {
    displayPopup.value = false
}

watch(() => props.isShowPopup, (newValue) => { 
  if (newValue) {
    displayPopup.value = true
    fadeOut.value = false

    setTimeout(() => {
      fadeOut.value = true
    }, 2000)

    setTimeout(() => {
      displayPopup.value = false
    }, 2500)
  }
}, {immediate: true})
</script>

<template>
  <div v-if="displayPopup" class="flex absolute rounded-sm shadow-[0_0.045rem_0.23rem_0_rgba(0,0,0,0.15)] overflow-hidden bg-[#54b15f] right-5 top-0 z-10"
    :class="{ 'animate-fade-out' : fadeOut }">
    <div class="flex bg-white ml-2 justify-center items-center gap-3 p-3">
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path fill="#54b15f" d="m10.6 13.8l-2.15-2.15q-.275-.275-.7-.275t-.7.275t-.275.7t.275.7L9.9 15.9q.3.3.7.3t.7-.3l5.65-5.65q.275-.275.275-.7t-.275-.7t-.7-.275t-.7.275zM12 22q-2.075 0-3.9-.788t-3.175-2.137T2.788 15.9T2 12t.788-3.9t2.137-3.175T8.1 2.788T12 2t3.9.788t3.175 2.137T21.213 8.1T22 12t-.788 3.9t-2.137 3.175t-3.175 2.138T12 22"/>
      </svg>
      <div>
        <p class="text-lg font-semibold text-[#54b15f]">Success</p>
        <p class="text-gray-700 itbms-message">{{ message }}</p>
      </div>
      <button @click="closePopup" class="text-gray-500 hover:text-gray-700 focus:outline-none">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
      </button>
    </div>
  </div>
</template>

<style scoped>
@keyframes fade-out {
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.animate-fade-out {
  animation: fade-out 0.5s forwards;
}
</style>
