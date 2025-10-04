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
    minlength: Number,
    invalidMessage: String,
    min: Number,
    max: Number,
    step: Number,
    trim: {
        type: Boolean,
        default: true
    },
    inputmode: String,
    pattern: String,
    limitLength: Number,
    readonly: { 
        type: Boolean,
        default: false
    },
    labelSize: {
        type: String,
        default: 'text-sm sm:text-base lg:text-lg'
    },
    inputSize: {
        type: String,
        default: 'text-sm md:text-base'
    }
})

const inputValue = defineModel()
const inputRef = ref(null)
const isValid = ref(true)

const emit = defineEmits(['disabledButton'])

function handleBlur() {
    if (inputValue.value === '' && props.required) {
        isValid.value = false
    } else if (inputValue.value.length > props.maxlength || (props.min && inputValue.value.length < props.min)) {
        isValid.value = false
    } else if (props.pattern) {
        isValid.value = new RegExp(props.pattern).test(inputValue.value)
    } else if (inputRef.value && inputValue.value !== '') {
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
    <div class="font-rubik flex flex-col w-full">
        <label class="text-[#332A1E] font-medium mb-1" :class="labelSize">{{ label }}
            <span v-if="required === true" class="text-red-700">*</span>
        </label>
        <textarea v-if="inputType === 'textarea'" v-model.trim="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :readonly="readonly"
            :class="[
                `${className} appearance-none py-2 md:py-3 w-full ${inputSize} text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-3 md:px-5 mt-1 min-h-[6rem] selection:bg-[#2684FF]/30 focus:outline-none`,
                { 'border-red-400' : !isValid },
                readonly ? 'bg-gray-100' : 'bg-white focus:ring-2 focus:ring-[#2684FF]'
            ]"/>
        <input v-else-if="trim" :type="inputType" :inputmode="inputmode" :required="required" v-model.trim="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :maxlength="limitLength" :min="min" :max="max" :step="step" :readonly="readonly"
            :class="[
                `${className} h-[2rem] md:h-[2.5rem] lg:h-[2.75rem] appearance-none w-full ${inputSize} text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-3 md:px-5 mt-1 selection:bg-[#2684FF]/30 focus:outline-none`,
                { 'border-red-400' : !isValid },
                readonly ? 'bg-gray-100' : 'bg-white focus:ring-2 focus:ring-[#2684FF]'
            ]"/>
        <input v-else :type="inputType" :required="required" :inputmode="inputmode" v-model="inputValue" ref="inputRef" :placeholder="placeholder" @blur="handleBlur" :min="min" :max="max" :step="step" :maxlength="limitLength" :readonly="readonly"
            :class="[
                `${className} h-[2rem] md:h-[2.5rem] lg:h-[2.75rem] appearance-none w-full ${inputSize} text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-3 md:px-5 mt-1 selection:bg-[#2684FF]/30 focus:outline-noneq`,
                { 'border-red-400' : !isValid },
                readonly ? 'bg-gray-100' : 'bg-white focus:ring-2 focus:ring-[#2684FF]'
            ]"/>
	    <div class="flex flex-col sm:flex-row sm:justify-between sm:items-center mt-1 sm:mt-1.5 gap-1 sm:gap-2">
            <p v-if="!isValid" class="itbms-message text-xs text-red-400">{{ invalidMessage }}</p>
            <p :class="['text-xs text-gray-500 ml-auto', {'text-red-400' : inputValue.length > props.maxlength}]" v-if="typeof inputValue === 'string'">{{ charCount }}</p>
        </div>
    </div>
</template>

<style scoped></style>
