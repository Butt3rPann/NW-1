<script setup>
const props = defineProps({
  message: {
    type: String,
    required: true
  }
})

defineEmits(['delete', 'close'])

</script>

<template>
<div class="flex justify-center items-center w-screen h-screen bg-[#000000]/50 z-20 top-7 fixed">
    <div class="bg-white relative flex justify-center items-center w-fit h-fit rounded-md py-10 px-17 shadow-[0_0.1rem_0.5rem_0_rgba(0,0,0,0.20)]">
        <button class="absolute text-gray-500 hover:text-gray-700 top-5 right-5" @click="$emit('close')">   
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
        </button>
        <div class="flex flex-col justify-center items-center gap-4">
            <div class="bg-[#FFE3E4] p-3 rounded-full w-fit">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 448 512">
                    <path fill="#DC2524" d="M432 32H312l-9.4-18.7A24 24 0 0 0 281.1 0H166.8a23.72 23.72 0 0 0-21.4 13.3L136 32H16A16 16 0 0 0 0 48v32a16 16 0 0 0 16 16h416a16 16 0 0 0 16-16V48a16 16 0 0 0-16-16M53.2 467a48 48 0 0 0 47.9 45h245.8a48 48 0 0 0 47.9-45L416 128H32z"/>
                </svg>
            </div>
            <div class="flex flex-col justify-center items-center">
                <p class="text-lg font-semibold text-[#332A1E]">Confirm Delete</p>
                <p class="text-[#332A1E]/80 itbms-message">{{ message }}</p>
            </div>
            <div class=" space-x-7">
                <button class="itbms-cancel-button bg-white text-[#332A1E]/80 border-2 border-[#332A1E]/70 rounded-md w-30 py-2 place-content-center" @click="$emit('close')">Cancel</button>
                <button class="itbms-confirm-button bg-[#DC2524] border-2 border-[#DC2524] text-white rounded-md w-30 py-2 place-content-center" @click="$emit('delete')">Delete</button>
            </div>
        </div>
    </div>
</div>
</template>
