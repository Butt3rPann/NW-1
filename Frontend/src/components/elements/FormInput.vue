<script setup>
defineProps({
    label: {
        type: String,
        required: true
    },
    required: Boolean,
    inputType: {
        type: String,
        required: true
    },
    placeholder: String
})

const inputValue = defineModel()
</script>
 
<template>
    <div class="font-rubik flex flex-col">
        <label class="text-[#332A1E] font-medium text-lg">{{ label }}
            <span v-if="required === true" class="text-red-700">*</span>
        </label>
        <input :type="inputType" :required="required" v-model="inputValue" :placeholder="placeholder"
            class="h-[2.75rem] appearance-none w-full text-base text-[#332A1E]/80 bg-white border border-[#332A1E]/20 rounded-xs px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF]" />
    </div>
</template>
 
<style scoped>

</style>