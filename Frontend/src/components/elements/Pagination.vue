<script setup>
import { computed, defineProps, defineEmits } from 'vue'

const props = defineProps({
    totalPage: Number,
    currentPage: Number,
    currentSize: Number,
})
const emits = defineEmits(['goToPage', 'prevPage', 'nextPage', 'lastPage'])
const pageNumbers = computed(() => {
  const numbers = []
  let startNumber = Math.max(1, props.currentPage - 9)
  const endNumber = Math.min(props.totalPage, startNumber + 9)
  if (endNumber - startNumber < 9) {
    startNumber = Math.max(1, endNumber - 9)
  }
  for (let i = startNumber; i <= endNumber; i++) {
    numbers.push(i)
  }
  return numbers
})
</script>
<template>
    <div v-show="totalPage > 1" class="flex flex-wrap justify-center items-center gap-2 mt-8">
        <button @click="$emit('goToPage', 1)" :disabled="currentPage === 1" :class="['itbms-page-first flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
        currentPage === 1? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                <path d="m11 17-5-5 5-5"></path>
                <path d="m18 17-5-5 5-5"></path>
            </svg>
        </button>
        <button @click="$emit('prevPage', response.first)" :disabled="currentPage === 1" :class="['itbms-page-prev flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
        currentPage === 1? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                <path d="m15 18-6-6 6-6"></path>
            </svg>
        </button>
        <button @click="$emit('goToPage', number)" v-for="(number, index) in pageNumbers" :key="number" :class="[`itbms-page-${index} flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border text-sm md:text-base`,
        currentPage === number? 'bg-[#6F879C] text-white border-[#6F879C]': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
            {{ number }}
        </button>
        <button @click="$emit('nextPage', response.last)" :disabled="currentPage === totalPage" :class="['itbms-page-next flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
        currentPage === totalPage? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                <path d="m9 18 6-6-6-6"></path>
            </svg>
        </button>
        <button @click="$emit('lastPage', totalPage)" :disabled="currentPage === totalPage" :class="['itbms-page-last flex items-center justify-center w-8 h-8 md:w-10 md:h-10 rounded-md border',
        currentPage === totalPage? 'text-gray-400 border-gray-200 cursor-not-allowed': 'text-[#332A1E] border-gray-300 hover:bg-gray-100']">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 md:w-5 md:h-5">
                <path d="m13 17 5-5-5-5"></path>
                <path d="m6 17 5-5-5-5"></path>
            </svg>
        </button>
    </div>
</template>
<style>
</style>