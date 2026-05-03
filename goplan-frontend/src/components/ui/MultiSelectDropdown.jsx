import React, { useState, useRef, useEffect } from 'react';
import { ChevronDown, Check } from 'lucide-react';

export function MultiSelectDropdown({ options, value, onChange, label, required }) {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const toggleOption = (optionId) => {
    const newValue = value.includes(optionId)
      ? value.filter((id) => id !== optionId)
      : [...value, optionId];
    onChange(newValue);
  };

  const selectedText = value.length === 0
    ? 'Select features...'
    : value.length === options.length
    ? 'All Features Selected'
    : `${value.length} feature${value.length > 1 ? 's' : ''} selected`;

  return (
    <div className="space-y-2" ref={dropdownRef}>
      {label && (
        <label className="block text-sm font-medium text-zinc-300">
          {label} {required && <span className="text-red-500">*</span>}
        </label>
      )}
      <div className="relative">
        <button
          type="button"
          onClick={() => setIsOpen(!isOpen)}
          className={`
            w-full flex items-center justify-between px-3 py-2 
            bg-zinc-950 border border-zinc-800 rounded-md 
            text-sm text-zinc-100 placeholder-zinc-500 
            focus:outline-none focus:ring-1 focus:ring-zinc-700 focus:border-zinc-700
            transition-colors
          `}
        >
          <span>{selectedText}</span>
          <ChevronDown className={`w-4 h-4 text-zinc-400 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
        </button>

        {isOpen && (
          <div className="absolute z-10 w-full mt-1 bg-zinc-900 border border-zinc-800 rounded-md shadow-lg">
            <ul className="py-1 max-h-60 overflow-auto focus:outline-none">
              {options.map((option) => {
                const isSelected = value.includes(option.id);
                return (
                  <li
                    key={option.id}
                    onClick={() => toggleOption(option.id)}
                    className={`
                      cursor-pointer select-none relative py-2 pl-3 pr-9 
                      hover:bg-zinc-800 transition-colors
                      ${isSelected ? 'text-zinc-50' : 'text-zinc-300'}
                    `}
                  >
                    <span className="block truncate text-sm">{option.label}</span>
                    {isSelected && (
                      <span className="absolute inset-y-0 right-0 flex items-center pr-3 text-zinc-50">
                        <Check className="w-4 h-4" />
                      </span>
                    )}
                  </li>
                );
              })}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
