<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
    label: {
        type: String,
        required: true
    },
    required: Boolean,
    inputType: {
        type: String,
        required: true
    },
    field: {
        type: String,
        required: true
    },
    placeholder: String,
    className: String,
    maxlength: Number,
    invalidMessage: String,
    min: Number,
    max: Number,
    step: Number
})

const inputValue = defineModel()
const inputRef = ref(null)
const isValid = ref(true)

const emit = defineEmits(['disabledButton'])

function handleBlur() {
    if (typeof inputValue.value === 'string') inputValue.value = inputValue.value.trim()

    if (inputValue.value.trim() === '' && props.required) {
        isValid.value = false
    } else if (inputValue.value.trim().length > props.maxlength) {
        isValid.value = false
    } else if (inputRef.value && inputValue.value.trim() !== '') {
        isValid.value = inputRef.value.checkValidity() 
    } else {
        isValid.value = true
    }

    emit('disabledButton', props.field, !isValid.value)
}

const charCount = computed(() => {
  if (typeof inputValue.value !== 'string') return ''
  return props.maxlength
    ? `${inputValue.value.length} / ${props.maxlength}`
    : ''
})
</script>

<template>
    <div class="font-rubik flex flex-col">
        <label class="text-[#332A1E] font-medium text-lg">{{ label }}
            <span v-if="required === true" class="text-red-700">*</span>
        </label>
        <textarea v-if="inputType === 'textarea'" v-model="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur"
            :class="[
                `${className} appearance-none py-3 w-full text-base text-[#332A1E]/80 border-[#332A1E]/20 bg-white border rounded-xs px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF] min-h-[6rem] selection:bg-[#2684FF]/30`,
                { 'border-red-400' : !isValid }
            ]"/>
        <input v-else :type="inputType" :required="required" v-model="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :min="min" :max="max" :step="step"
            :class="[
                `${className} h-[2.75rem] appearance-none w-full text-base bg-white text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF] selection:bg-[#2684FF]/30`,
                { 'border-red-400' : !isValid }
            ]"/>
	<div class="flex justify-between items-center mt-1">
            <p v-if="!isValid" class="itbms-message text-xs text-red-400">{{ invalidMessage }}</p>
            <p :class="['text-xs text-gray-500 ml-auto', {'text-red-400' : inputValue.length > props.maxlength}]" v-if="typeof inputValue === 'string'">{{ charCount }}</p>
        </div>
    </div>
</template>

<style scoped></style>
