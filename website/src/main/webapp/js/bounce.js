// Bounce the given element to the given location, using its own effects.js queue (based
// on the element's id)

function bounceInQueue( toBounce, bounceTo )
{
	Effect.Queues.get( toBounce.id ).each( function( effect ) { effect.cancel(); });				
	new Effect.Move( toBounce, {y: bounceTo, transition: Effect.Transitions.spring, mode: 'absolute', queue: { scope: toBounce.id }})
}
