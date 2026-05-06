import React, { useState } from 'react';
import { Trash2, Plus, AlertCircle } from 'lucide-react';
import { ModelField } from './ModelField';

export function ModelCard({ model, onChange, onRemove }) {
  const [error, setError] = useState(null);

  const handleNameChange = (e) => {
    const val = e.target.value;
    if (!val) {
      setError('Model name cannot be empty');
    } else if (!/^[A-Z][a-zA-Z0-9]*$/.test(val)) {
      setError('Model name should be PascalCase');
    } else {
      setError(null);
    }
    onChange({ ...model, name: val });
  };

  const addField = () => {
    const newField = { id: crypto.randomUUID(), name: '', type: 'string' };
    onChange({ ...model, fields: [...model.fields, newField] });
  };

  const updateField = (index, updatedField) => {
    const newFields = [...model.fields];
    newFields[index] = updatedField;
    onChange({ ...model, fields: newFields });
  };

  const removeField = (index) => {
    onChange({ ...model, fields: model.fields.filter((_, i) => i !== index) });
  };

  return (
    <div className="bg-zinc-900 border-2 border-zinc-800 p-4 relative hover:border-orange-500 transition-colors">
      <div className="flex items-start justify-between mb-4">
        <div className="flex-1 mr-4">
          <input
            type="text"
            value={model.name}
            onChange={handleNameChange}
            placeholder="ModelName"
            className="w-full text-lg font-bold bg-transparent border-b-2 border-zinc-700 focus:outline-none focus:border-orange-500 pb-1 text-zinc-50 placeholder-zinc-500 transition-colors"
          />
          {error && (
            <span className="text-xs text-red-500 flex items-center mt-1">
              <AlertCircle className="w-3 h-3 mr-1" />
              {error}
            </span>
          )}
        </div>
        <button
          type="button"
          onClick={onRemove}
          className="p-1.5 text-zinc-500 hover:text-red-500 hover:bg-red-950/30 transition-colors border-2 border-transparent hover:border-red-900/50"
          title="Remove Model"
        >
          <Trash2 className="w-5 h-5" />
        </button>
      </div>

      <div className="space-y-1 mb-4">
        <div className="text-xs font-bold text-zinc-500 uppercase tracking-wider mb-2">Fields</div>
        {model.fields.map((field, index) => (
          <ModelField
            key={field.id}
            field={field}
            onChange={(updated) => updateField(index, updated)}
            onRemove={() => removeField(index)}
          />
        ))}
        {model.fields.length === 0 && (
          <div className="text-sm text-zinc-600 italic py-2">No fields added yet.</div>
        )}
      </div>

      <button
        type="button"
        onClick={addField}
        className="text-sm font-bold text-orange-500 flex items-center hover:text-orange-400"
      >
        <Plus className="w-4 h-4 mr-1" />
        Add Field
      </button>
    </div>
  );
}
