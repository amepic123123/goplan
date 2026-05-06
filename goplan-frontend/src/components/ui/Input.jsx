import React from 'react';

export function Input({ label, id, className = '', ...props }) {
  return (
    <div className="flex flex-col space-y-1.5">
      {label && (
        <label htmlFor={id} className="text-sm font-bold text-zinc-50">
          {label}
        </label>
      )}
      <input
        id={id}
        className={`
          w-full px-3 py-2 bg-zinc-950 border-2 border-zinc-800 rounded-none
          text-zinc-50 text-sm placeholder-zinc-500
          focus:outline-none focus:border-orange-500
          transition-colors
          ${className}
        `}
        {...props}
      />
    </div>
  );
}
