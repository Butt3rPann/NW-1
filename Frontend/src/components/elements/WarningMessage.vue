<script setup>
const props = defineProps({
  message: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['close'])
</script>
 
<template>
<div class="flex justify-center items-center w-screen h-screen bg-[#000000]/50 top-7 fixed">
    <div class="bg-white relative flex justify-center items-center w-fit h-fit rounded-md py-10 px-11 sm:px-17 shadow-[0_0.1rem_0.5rem_0_rgba(0,0,0,0.20)]">
        <button class="absolute text-gray-500 hover:text-gray-700 top-5 right-5" @click="$emit('close')">   
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
        </button>
        <div class="flex flex-col justify-center items-center gap-4">
            <div class="bg-[#FFE3E4] p-3 rounded-full w-fit">
                <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="23 0 448 512" 
                fill="#DC2524"  version="1.1" id="Layer_1"  xmlns:xlink="http://www.w3.org/1999/xlink" xml:space="preserve" stroke="#DC2524"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <g> <g>
                <path d="M506.43,421.536L291.573,49.394c-15.814-27.391-55.327-27.401-71.147,0L5.568,421.536 c-15.814,27.391,3.934,61.616,35.574,61.616h429.714C502.485,483.153,522.25,448.938,506.43,421.536z M274.821,385.034 c0,10.394-8.427,18.821-18.821,18.821s-18.821-8.427-18.821-18.821v-11.239c0-10.394,8.427-18.821,18.821-18.821 s18.821,8.427,18.821,18.821V385.034z M274.821,311.702c0,10.394-8.427,18.821-18.821,18.821s-18.821-8.427-18.821-18.821v-107.89 c0-10.394,8.427-18.821,18.821-18.821s18.821,8.427,18.821,18.821V311.702z"></path> </g> </g> </g></svg>
            </div>
            <div class="flex flex-col justify-center items-center">
                <p class="text-base md:text-xl font-semibold text-[#332A1E]">Warning</p>
                <p class="text-sm md:text-lg text-[#332A1E]/80 itbms-message sm:bg-white w-50 sm:w-64 md:w-94 lg:w-full text-center">{{ message }}</p>
            </div>
            <div class=" space-x-7">
                <button class="itbms-cancel-button text-sm md:text-lg bg-white text-[#332A1E]/80 border-2 border-[#332A1E]/70 rounded-md w-30 py-2 place-content-center cursor-pointer" @click="$emit('close')">Cancel</button>
            </div>
        </div>
    </div>
</div>
</template>

 
<style scoped>

</style>