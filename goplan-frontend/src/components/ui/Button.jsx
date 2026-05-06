import React from 'react';
import { Loader2 } from 'lucide-react';

export function Button({ 
  children, 
  loading = false, 
  disabled = false, 
  className = '', 
  ...props 
}) {
  return (
    <button
      disabled={disabled || loading}
      className={`
        relative inline-flex items-center justify-center 
        px-4 py-2 font-medium text-sm rounded-none transition-colors
        bg-red-600 text-white hover:bg-red-700
        active:bg-red-800 disabled:opacity-50 disabled:pointer-events-none
        ${className}
      `}
      {...props}
    >
      {loading ? (
        <>
          <Loader2 className="w-4 h-4 mr-2 animate-spin" />
          Generating Engine...
        </>
      ) : (
        children
      )}
    </button>
  );
}
