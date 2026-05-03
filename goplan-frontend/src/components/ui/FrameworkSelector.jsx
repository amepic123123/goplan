import React from 'react';
import { CheckCircle2, Circle, Check } from 'lucide-react';

export function FrameworkSelector({ options, value, onChange, featureOptions, features, onFeaturesChange }) {
  const toggleFeature = (e, featureId) => {
    e.stopPropagation();
    const newFeatures = features.includes(featureId)
      ? features.filter((id) => id !== featureId)
      : [...features, featureId];
    onFeaturesChange(newFeatures);
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
      {options.map((option) => {
        const isSelected = value === option.id;
        
        return (
          <div
            key={option.id}
            onClick={() => onChange(option.id)}
            className={`
              relative flex flex-col items-start p-4 border rounded-md text-left transition-all cursor-pointer overflow-hidden
              ${isSelected 
                ? 'bg-zinc-800 border-zinc-50' 
                : 'bg-zinc-900 border-zinc-800 hover:border-zinc-600 hover:bg-zinc-800/50'
              }
            `}
          >
            <div className="flex items-start w-full">
              <div className="flex-shrink-0 mt-0.5">
                {isSelected ? (
                  <CheckCircle2 className="w-5 h-5 text-zinc-50" />
                ) : (
                  <Circle className="w-5 h-5 text-zinc-500" />
                )}
              </div>
              <div className="ml-3">
                <span className={`block text-sm font-medium ${isSelected ? 'text-zinc-50' : 'text-zinc-200'}`}>
                  {option.title}
                </span>
                <span className={`block text-sm mt-1 ${isSelected ? 'text-zinc-300' : 'text-zinc-400'}`}>
                  {option.description}
                </span>
              </div>
            </div>

            <div 
              className={`w-full grid transition-all duration-300 ease-in-out ${isSelected ? 'grid-rows-[1fr] opacity-100 mt-4' : 'grid-rows-[0fr] opacity-0 mt-0'}`}
            >
              <div className="overflow-hidden">
                <div className="pt-3 border-t border-zinc-700/50">
                  <span className="block text-xs font-semibold text-zinc-400 mb-2 uppercase tracking-wider">Included Features</span>
                  <div className="grid grid-cols-2 gap-1">
                    {featureOptions.map((feature) => {
                      const isFeatureSelected = features.includes(feature.id);
                      return (
                        <div 
                          key={feature.id} 
                          className={`flex items-center py-1.5 px-2 rounded-md cursor-pointer transition-colors ${isFeatureSelected ? 'bg-zinc-700/40' : 'hover:bg-zinc-700/20'}`}
                          onClick={(e) => toggleFeature(e, feature.id)}
                        >
                          <div className={`w-4 h-4 rounded-sm border flex items-center justify-center mr-3 transition-colors ${isFeatureSelected ? 'bg-zinc-50 border-zinc-50 text-zinc-900' : 'border-zinc-500 bg-transparent'}`}>
                            {isFeatureSelected && <Check className="w-3 h-3" />}
                          </div>
                          <span className={`text-sm ${isFeatureSelected ? 'text-zinc-100' : 'text-zinc-400'}`}>
                            {feature.label}
                          </span>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
