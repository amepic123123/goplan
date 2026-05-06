import React from 'react';
import { Plus, Check } from 'lucide-react';
import { ModelCard } from './ModelCard';
import { Button } from './Button';

export function ArchitectureBuilder({ models, onModelsChange, featureOptions, features, onFeaturesChange }) {
  const addModel = () => {
    const newModel = {
      id: crypto.randomUUID(),
      name: `NewModel${models.length + 1}`,
      fields: []
    };
    onModelsChange([...models, newModel]);
  };

  const updateModel = (index, updatedModel) => {
    const newModels = [...models];
    newModels[index] = updatedModel;
    onModelsChange(newModels);
  };

  const removeModel = (index) => {
    onModelsChange(models.filter((_, i) => i !== index));
  };

  const toggleFeature = (featureId) => {
    const newFeatures = features.includes(featureId)
      ? features.filter((id) => id !== featureId)
      : [...features, featureId];
    onFeaturesChange(newFeatures);
  };

  return (
    <div className="space-y-6">
      <div className="pb-4 border-b-2 border-zinc-800">
        <h2 className="text-xl font-bold text-zinc-50 mb-3">Feature Architecture</h2>
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-2">
          {featureOptions.map((feature) => {
            const isFeatureSelected = features.includes(feature.id);
            return (
              <div 
                key={feature.id} 
                className={`flex flex-col items-center justify-center p-3 cursor-pointer transition-colors border-2 ${isFeatureSelected ? 'bg-zinc-800 border-orange-500' : 'bg-zinc-900 border-zinc-800 hover:border-zinc-700'}`}
                onClick={() => toggleFeature(feature.id)}
              >
                <div className={`h-6 mb-1 flex items-center justify-center transition-colors ${isFeatureSelected ? 'text-orange-500' : 'text-zinc-600'}`}>
                  {isFeatureSelected ? (
                    <Check className="w-5 h-5" strokeWidth={3} />
                  ) : (
                    <div className="w-3 h-3 rounded-full border-2 border-zinc-600" />
                  )}
                </div>
                <span className={`text-xs font-bold text-center uppercase tracking-wider ${isFeatureSelected ? 'text-zinc-50' : 'text-zinc-500'}`}>
                  {feature.label}
                </span>
              </div>
            );
          })}
        </div>
      </div>

      <div className="space-y-4">
        <div className="flex items-center justify-between pb-2 border-b-2 border-zinc-800">
          <h2 className="text-xl font-bold text-zinc-50">Data Models</h2>
          <Button type="button" onClick={addModel} className="!bg-orange-500 hover:!bg-orange-600 active:!bg-orange-700 text-white !py-1.5 !px-3">
            <Plus className="w-4 h-4 mr-1" />
            Add Model
          </Button>
        </div>
        
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-4">
          {models.map((model, index) => (
            <ModelCard
              key={model.id}
              model={model}
              onChange={(updated) => updateModel(index, updated)}
              onRemove={() => removeModel(index)}
            />
          ))}
          {models.length === 0 && (
            <div className="col-span-full py-8 text-center border-2 border-dashed border-zinc-800 bg-zinc-900/50">
              <p className="text-zinc-500 mb-4">No models defined yet.</p>
              <Button type="button" onClick={addModel} className="!bg-orange-500 hover:!bg-orange-600 active:!bg-orange-700 text-white">
                <Plus className="w-4 h-4 mr-2" />
                Create First Model
              </Button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
