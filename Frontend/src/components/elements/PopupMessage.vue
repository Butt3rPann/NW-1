<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
  message: {
    type: String,
    required: true,
  },
  isShowPopup: {
    type: Boolean,
    required: true
  },
  isSuccess: {
    type: Boolean,
    default: true
  }
})

const isSuccess = computed(() => props.isSuccess)

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
  <div v-if="displayPopup" class="flex absolute rounded-sm shadow-[0_0.1rem_0.5rem_0_rgba(0,0,0,0.15)] overflow-hidden right-1 md:right-5 top-0 z-10"
    :class="[{ 'animate-fade-out' : fadeOut }, isSuccess ? 'bg-[#54b15f]' : 'bg-[#DC2524]']">
    <div class="flex bg-white ml-2 justify-center items-center gap-3 p-3">
      <svg xmlns="http://www.w3.org/2000/svg" width="20" md:width="24" height="20" md:height="24" viewBox="0 0 24 24">
        <path v-if="isSuccess" fill="#54b15f" d="m10.6 13.8l-2.15-2.15q-.275-.275-.7-.275t-.7.275t-.275.7t.275.7L9.9 15.9q.3.3.7.3t.7-.3l5.65-5.65q.275-.275.275-.7t-.275-.7t-.7-.275t-.7.275zM12 22q-2.075 0-3.9-.788t-3.175-2.137T2.788 15.9T2 12t.788-3.9t2.137-3.175T8.1 2.788T12 2t3.9.788t3.175 2.137T21.213 8.1T22 12t-.788 3.9t-2.137 3.175t-3.175 2.138T12 22"/>
        <path v-else fill="#DC2524" fill-rule="evenodd" d="M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2S2 6.477 2 12s4.477 10 10 10m4.066-14.066a.75.75 0 0 1 0 1.06L13.06 12l3.005 3.005a.75.75 0 0 1-1.06 1.06L12 13.062l-3.005 3.005a.75.75 0 1 1-1.06-1.06L10.938 12L7.934 8.995a.75.75 0 1 1 1.06-1.06L12 10.938l3.005-3.005a.75.75 0 0 1 1.06 0" clip-rule="evenodd" />
      </svg>
      <div>
        <p class="text-xs md:text-lg font-semibold">
          <span v-if="isSuccess" class="text-[#54b15f]">Success</span>
          <span v-else class="text-[#DC2524]">Failure</span>
        </p>
        <p class="text-gray-700 text-xs md:text-lg itbms-message">{{ message }}</p>
      </div>
      <button @click="closePopup" class="text-gray-500 hover:text-gray-700 focus:outline-none">
        <svg class="w-4 md:w-5 h-4 md:h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
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
