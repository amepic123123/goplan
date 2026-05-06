import React from 'react';
import { CheckCircle2, Circle } from 'lucide-react';

export function FrameworkSelector({ options, value, onChange }) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
      {options.map((option) => {
        const isSelected = value === option.id;
        
        return (
          <div
            key={option.id}
            onClick={() => onChange(option.id)}
            className={`
              relative flex flex-col items-start p-4 border-2 rounded-none text-left transition-colors cursor-pointer overflow-hidden
              ${isSelected 
                ? 'bg-zinc-900 border-orange-500' 
                : 'bg-zinc-900 border-zinc-800 hover:border-zinc-700'
              }
            `}
          >
            <div className="flex items-start w-full">
              <div className="flex-shrink-0 mt-0.5">
                {isSelected ? (
                  <CheckCircle2 className="w-5 h-5 text-orange-500" />
                ) : (
                  <Circle className="w-5 h-5 text-zinc-500" />
                )}
              </div>
              <div className="ml-3">
                <span className={`block text-sm font-bold ${isSelected ? 'text-zinc-50' : 'text-zinc-300'}`}>
                  {option.title}
                </span>
                <span className={`block text-sm mt-1 ${isSelected ? 'text-zinc-400' : 'text-zinc-500'}`}>
                  {option.description}
                </span>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
