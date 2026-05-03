import React from 'react';

export function Input({ label, id, className = '', ...props }) {
  return (
    <div className="flex flex-col space-y-1.5">
      {label && (
        <label htmlFor={id} className="text-sm font-medium text-zinc-300">
          {label}
        </label>
      )}
      <input
        id={id}
        className={`
          w-full px-3 py-2 bg-zinc-900 border border-zinc-800 rounded-md
          text-zinc-50 text-sm placeholder-zinc-500
          focus:outline-none focus:ring-1 focus:ring-zinc-500 focus:border-zinc-500
          transition-all
          ${className}
        `}
        {...props}
      />
    </div>
  );
}
