import React, { useState, useEffect } from 'react';
import { Trash2, AlertCircle } from 'lucide-react';

const FIELD_TYPES = ['string', 'int', 'long', 'double', 'boolean', 'date', 'email', 'phone'];

export function ModelField({ field, onChange, onRemove }) {
  const [error, setError] = useState(null);

  useEffect(() => {
    // Validation for camelCase
    if (field.name && !/^[a-z][a-zA-Z0-9]*$/.test(field.name)) {
      setError('Field name must be camelCase');
    } else {
      setError(null);
    }
  }, [field.name]);

  return (
    <div className="flex flex-col space-y-1 mt-2">
      <div className="flex items-center space-x-2">
        <input
          type="text"
          value={field.name}
          onChange={(e) => onChange({ ...field, name: e.target.value })}
          placeholder="fieldName"
          className="flex-1 px-3 py-1.5 bg-zinc-950 border-2 border-zinc-800 text-zinc-50 text-sm focus:outline-none focus:border-orange-500 transition-colors"
        />
        <select
          value={field.type}
          onChange={(e) => onChange({ ...field, type: e.target.value })}
          className="w-28 px-2 py-1.5 bg-zinc-950 border-2 border-zinc-800 text-zinc-50 text-sm focus:outline-none focus:border-orange-500 transition-colors appearance-none cursor-pointer"
        >
          {FIELD_TYPES.map(t => (
            <option key={t} value={t}>{t}</option>
          ))}
        </select>
        <button
          type="button"
          onClick={onRemove}
          className="p-1.5 text-zinc-500 hover:text-red-500 hover:bg-red-950/30 transition-colors border-2 border-transparent hover:border-red-900/50"
        >
          <Trash2 className="w-4 h-4" />
        </button>
      </div>
      {error && (
        <span className="text-xs text-red-500 flex items-center">
          <AlertCircle className="w-3 h-3 mr-1" />
          {error}
        </span>
      )}
    </div>
  );
}
