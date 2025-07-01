<script setup>
import { ref } from 'vue';

const props = defineProps({
    label: {
        type: String,
        required: true
    },
    field: {
        type: String,
        required: true
    },
    options: Array,
    property: String,
    placeholder : String,
    className: String,
    invalidMessage: String
})

const inputValue = defineModel()
const isValid = ref(true)

const emit = defineEmits(['disabledButton'])

function handleBlur() {
    if (!inputValue.value.id) {
        isValid.value = false
    } else {
        isValid.value = true
    }

    emit('disabledButton', props.field, !isValid.value)
}</script>
 
<template>
    <div class="font-rubik flex flex-col w-full">
        <label class="text-[#332A1E] font-medium text-sm sm:text-base lg:text-lg mb-1 sm:mb-1.5">{{ label }}
            <span class="text-red-700">*</span>
        </label>
        <select v-model="inputValue" :value="inputValue" @blur="handleBlur"
        :class="[
            `${className} h-[2rem] md:h-[2.5rem] lg:h-[2.75rem] appearance-none w-full text-sm md:text-base text-[#332A1E]/80 bg-white border border-[#332A1E]/20 rounded-xs px-3 md:px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF]`,
            inputValue?.id ? 'text-[#332A1E]/80' : 'text-[#AEAAA6]',
            {'border-red-400' : !isValid}
        ]">
            <option disabled :value="{id: null, name: null}">{{ placeholder || 'Please select' }}</option>
	        <option :value="''">{{ '' }}</option>
            <option v-for="option in options" :key="option.id" :value="option">{{ option[property]}}</option>
        </select>
        <div class="mt-1 sm:mt-1.5">
            <p v-if="!isValid" class="itbms-message text-[0.65rem] md:text-xs ml-3 font-normal text-red-400">{{ invalidMessage }}</p>
        </div>
    </div>
</template>
 
<style scoped>

</style>
