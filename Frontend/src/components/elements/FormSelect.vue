<script setup>
defineProps({
    label: {
        type: String,
        required: true
    },
    options: Array,
    placeholder : String
})

const inputValue = defineModel()
</script>
 
<template>
    <div class="font-rubik">
        <label class="text-[#332A1E] font-medium text-lg">{{ label }}
            <span class="text-red-700">*</span>
        </label>
        <select v-model="inputValue" :value="inputValue"
        class="h-[2.75rem] appearance-none w-full text-base text-[#332A1E]/80 bg-white border border-[#332A1E]/20 rounded-xs px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF]"
        >
            <option distabled value="">{{ placeholder || 'Please select' }}</option>
            <option v-for="option in options" :key="option.value">{{ option }}</option>
        </select>
    </div>
</template>
 
<style scoped>

</style>